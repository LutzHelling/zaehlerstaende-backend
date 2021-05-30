package com.online.helling.zaehler.dataaccess;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.ManagedBean;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.transaction.TransactionalException;

import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cache.CacheManager;

import com.online.helling.zaehler.ZaehlerstaendeBackend;
import com.online.helling.zaehler.dataaccess.model.Jahresstand;
import com.online.helling.zaehler.dataaccess.model.Settings;
import com.online.helling.zaehler.dataaccess.model.Zaehler;
import com.online.helling.zaehler.dataaccess.model.Zaehlerstand;
import com.online.helling.zaehler.dataaccess.model.Zaehlerverbrauch;
import com.online.helling.zaehler.dataaccess.model.ZaehlerverbrauchId;
import com.online.helling.zaehler.dataaccess.repository.SettingsRepository;
import com.online.helling.zaehler.dataaccess.repository.ZaehlerRepository;
import com.online.helling.zaehler.dataaccess.repository.ZaehlerstandRepository;
import com.online.helling.zaehler.dataaccess.repository.ZaehlerverbrauchRepository;
import com.online.helling.zaehler.util.Medium;
import com.online.helling.zaehler.util.Zeitreihe;

@ManagedBean
public class ZaehlerAccess implements Serializable {

	private static final long serialVersionUID = 4123259548345465244L;

	private static final DecimalFormat ZWEISTELLIG = new DecimalFormat("00");

	private static final DecimalFormat df = new DecimalFormat("0.000");

	@Autowired(required = true)
	CacheManager cacheManager;

	@Autowired(required = true)
	protected transient EntityManager em;

	@Autowired(required = true)
	protected transient SettingsRepository settingsRepository;

	@Autowired(required = true)
	protected transient ZaehlerRepository zRepository;

	@Autowired(required = true)
	protected transient ZaehlerstandRepository zsRepository;

	@Autowired(required = true)
	protected transient ZaehlerverbrauchRepository zvRepository;

	private static Integer differenceInMonths(Calendar beginningDate, Calendar endingDate) {
		if (beginningDate == null || endingDate == null) {
			return 0;
		}
		int m1 = beginningDate.get(Calendar.YEAR) * 12 + beginningDate.get(Calendar.MONTH);
		int m2 = endingDate.get(Calendar.YEAR) * 12 + endingDate.get(Calendar.MONTH);
		return m2 - m1;
	}

	public static Integer differenceInMonths(Date beginningDate, Date endingDate) {
		if (beginningDate == null || endingDate == null) {
			return 0;
		}
		Calendar cal1 = new GregorianCalendar();
		cal1.setTime(beginningDate);
		Calendar cal2 = new GregorianCalendar();
		cal2.setTime(endingDate);
		return differenceInMonths(cal1, cal2);
	}

	public static long getDifferenceDays(Date d1, Date d2) {
		long diff = d2.getTime() - d1.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	public static int getMonatsInt(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return Integer
				.parseInt(String.valueOf(cal.get(Calendar.YEAR)) + ZWEISTELLIG.format(cal.get(Calendar.MONTH) + 1l));
	}

	private Zaehlerverbrauch findBezug(int bezug, List<Zaehlerverbrauch> verbraeuche) {
		for (Zaehlerverbrauch zaehlerverbrauch : verbraeuche) {
			if (zaehlerverbrauch.getAktMonatNumeric() == bezug)
				return zaehlerverbrauch;
		}
		return null;
	}

	public EnumMap<Medium, Zeitreihe<BigDecimal>> getJahresstaende(Date start) {
		EnumMap<Medium, Zeitreihe<BigDecimal>> ret = new EnumMap<>(Medium.class);

		for (Medium medium : Medium.values()) {
			ret.put(medium, getJahresstaende(medium, start));
		}
		return ret;
	}

	public EnumMap<Medium, BigDecimal> getJahresstaendeRaw(int year, Medium medium) {
		EnumMap<Medium, BigDecimal> ret = new EnumMap<>(Medium.class);
		ret.put(medium, getJahresstandRaw(medium, year));
		return ret;
	}

	public BigDecimal getJahresstandRaw(Medium medium, int year) {
		BigDecimal summeVerbrauchEinesJahres = zvRepository.sumVerbrauchByMediumAndJahr(medium.getTyp(), year);
		return summeVerbrauchEinesJahres;
	}

	public Zeitreihe<BigDecimal> getJahresstaende(final Medium medium, Date start) {
		int typ = medium.getTyp();
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Calendar calAktBezug = Calendar.getInstance();
		Zeitreihe<BigDecimal> staende = new Zeitreihe<>();

		cal.add(Calendar.MONTH, 0);
		int endeBezug = getMonatsInt(cal.getTime());

		cal.add(Calendar.MONTH, -11);
		int startBezug = getMonatsInt(cal.getTime());

		List<Zaehlerverbrauch> alleVerbraeucheEinesJahres = zvRepository.findAllByMediumImZeitraum(medium.getTyp(),
				startBezug, endeBezug);

		for (Zaehlerverbrauch zaehlerverbrauch : alleVerbraeucheEinesJahres) {
			normiereEintrag(zaehlerverbrauch);
		}

		for (int i = 0; i <= 11; i++) {

			cal.setTimeInMillis(start.getTime());

			// Vormonatsletzter
			cal.add(Calendar.MONTH, -1 * (i + 1));
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			calAktBezug.setTimeInMillis(cal.getTimeInMillis());
			int aktBezug = getMonatsInt(cal.getTime());

			// Letzter des Folgemonats
			cal.add(Calendar.MONTH, -1);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			int vorBezug = getMonatsInt(cal.getTime());

			// Monatsletzter
			// cal.add(Calendar.MONTH, +2);
			// cal.set(Calendar.DAY_OF_MONTH,
			// cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			// int nachBezug = getMonatsInt(cal.getTime());

			// cal.add(Calendar.MONTH, +1);
			// cal.set(Calendar.DAY_OF_MONTH,
			// cal.getActualMaximum(Calendar.DAY_OF_MONTH));

			// Erstmal den aktuellen Verbrauch ermitteln
			Zaehlerverbrauch verbrauch = findBezug(aktBezug, alleVerbraeucheEinesJahres);

			int diff;
			BigDecimal durchschnitt;
			if (verbrauch == null) {
				// Eine Ableselücke wurde entdeckt!

				// Den vorherigen Verbrauch
				Zaehlerverbrauch vorherigerVerbrauch = findBezug(vorBezug, alleVerbraeucheEinesJahres);
				if (vorherigerVerbrauch == null) {
					vorherigerVerbrauch = zvRepository.findNearestVerbrauchBefore(typ, vorBezug);
				}

				// Zaehlerverbrauch folgenderVerbrauch = findBezug(nachBezug,
				// alleVerbraeucheEinesJahres);
				// if (folgenderVerbrauch == null && nachBezug > 0) {
				// // Eine Ableselücke wurde entdeckt!
				// folgenderVerbrauch =
				// zvRepository.findNearestVerbrauchEqualOrAfter(typ,
				// nachBezug);
				// }

				verbrauch = new Zaehlerverbrauch();
				verbrauch.setId(new ZaehlerverbrauchId());
				verbrauch.setInterpoliert(true);

				if (vorherigerVerbrauch == null) {
					Calendar calTemp = Calendar.getInstance();
					calTemp.setTime(calAktBezug.getTime());
					calTemp.set(Calendar.DAY_OF_MONTH, calTemp.getMinimum(Calendar.DAY_OF_MONTH));
					diff = (int) getDifferenceDays(calTemp.getTime(), calAktBezug.getTime());
					durchschnitt = verbrauch.getTagesverbrauchImDurchschnitt();
				} else {
					diff = (int) getDifferenceDays(vorherigerVerbrauch.getId().getAktDate(), calAktBezug.getTime());
					durchschnitt = vorherigerVerbrauch.getTagesverbrauchImDurchschnitt();
				}
				diff = Math.min(diff, calAktBezug.getActualMaximum(Calendar.DAY_OF_MONTH));
				verbrauch.setAnzTage(diff);
				verbrauch.setTagesverbrauchImDurchschnitt(durchschnitt);
				verbrauch.setVorMonatNumeric(vorBezug);
				verbrauch.getId().setAktDate(calAktBezug.getTime());
				verbrauch.getId().setTyp(typ);
				verbrauch.setAktMonatNumeric(aktBezug);
				if (verbrauch.getZaehlerId() == null && vorherigerVerbrauch != null) {
					verbrauch.setZaehlerId(vorherigerVerbrauch.getZaehlerId());
				}
				verbrauch.setVerbrauch(
						BigDecimal.valueOf(diff).multiply(durchschnitt == null ? BigDecimal.ONE : durchschnitt));
			} else {
				// Verbrauchsaussage auf einen Monat normieren
				diff = verbrauch.getAnzTage() - calAktBezug.getActualMaximum(Calendar.DAY_OF_MONTH);
				if (diff < 0)
					diff = calAktBezug.getActualMaximum(Calendar.DAY_OF_MONTH);
				if (diff != 0) {
					durchschnitt = verbrauch.getTagesverbrauchImDurchschnitt();
					verbrauch.setAnzTage(calAktBezug.get(Calendar.DAY_OF_MONTH));
					verbrauch.setVerbrauch(BigDecimal.valueOf(verbrauch.getAnzTage()).multiply(durchschnitt));
					verbrauch.setInterpoliert(true);
				}
			}
			if (i > 0 && alleVerbraeucheEinesJahres.get(i - 1).isInterpoliert()) {
				Zaehlerverbrauch folgeMonatsverbrauch = alleVerbraeucheEinesJahres.get(i - 1);
				folgeMonatsverbrauch.setVorDate(verbrauch.getId().getAktDate());
				folgeMonatsverbrauch.setVorigerstand(verbrauch.getZaehlerstand());
				long diff2 = getDifferenceDays(folgeMonatsverbrauch.getVorDate(),
						folgeMonatsverbrauch.getId().getAktDate());
				folgeMonatsverbrauch.setAnzTage((int) diff2);
				folgeMonatsverbrauch.setVerbrauch(BigDecimal.valueOf(folgeMonatsverbrauch.getAnzTage())
						.multiply(folgeMonatsverbrauch.getTagesverbrauchImDurchschnitt() == null ? BigDecimal.ONE
								: folgeMonatsverbrauch.getTagesverbrauchImDurchschnitt()));
			}
			alleVerbraeucheEinesJahres.add(i, verbrauch);
		}

		for (Zaehlerverbrauch zaehlerverbrauch : alleVerbraeucheEinesJahres) {
			staende.add(setTimeToNull(zaehlerverbrauch.getId().getAktDate()), zaehlerverbrauch.getVerbrauch());
		}
		return staende;
	}

	public double getJahressumme(Medium medium, Date start) {
		Zeitreihe<BigDecimal> retReihe = getJahresstaende(medium, start);
		BigDecimal summe = BigDecimal.ZERO;
		for (Pair<Date, BigDecimal> pair : retReihe) {
			summe = summe.add(pair.getValue());
		}
		return summe.doubleValue();
	}

	public Zaehlerverbrauch getLastStand(final Medium medium) {
		return zvRepository.findNearestVerbrauchBeforeOrEqual(medium.getTyp(), getMonatsInt(new Date()));
	}

	public Zaehler getZaehler(Medium medium) {
		return zRepository.findOneByTyp(medium.getTyp());
	}

	public List<Jahresstand> listJahresstaende(Medium medium) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(new Date());
		return zvRepository.findAllJahresstaende(medium.getTyp(), cal.get(Calendar.YEAR));
	}

	public List<Zaehlerverbrauch> listAllVerbrauchsdaten() {
		List<Zaehlerverbrauch> ret = new ArrayList<>();
		Iterable<Zaehlerverbrauch> iter = zvRepository.findAllByOrderByAktMonatNumericDesc();
		for (Zaehlerverbrauch zaehlerverbrauch : iter) {
			ret.add(zaehlerverbrauch);
		}
		return ret;
	}

	private void normiereEintrag(Zaehlerverbrauch verbrauch) {
		Calendar ablesetermin = Calendar.getInstance();
		ablesetermin.setTime(verbrauch.getId().getAktDate());

		Calendar monatsende = Calendar.getInstance();
		monatsende.setTime(ablesetermin.getTime());
		monatsende.set(Calendar.DAY_OF_MONTH, monatsende.getActualMaximum(Calendar.DAY_OF_MONTH));

		int diffBisMonatsende = (int) getDifferenceDays(ablesetermin.getTime(), monatsende.getTime());
		BigDecimal verbrauchProTag = verbrauch.getTagesverbrauchImDurchschnitt();
		BigDecimal verbrauchInDiff = BigDecimal.valueOf(diffBisMonatsende).multiply(verbrauchProTag);
		verbrauch.getId().setAktDate(monatsende.getTime());
		verbrauch.setVerbrauch(verbrauch.getVerbrauch().add(verbrauchInDiff));
	}

	@Transactional
	public boolean saveAll(Map<Medium, BigDecimal> staende) {
		boolean ret = true;
		Date now = new Date();
		for (Map.Entry<Medium, BigDecimal> entry : staende.entrySet()) {
			if (zvRepository.findNearestVerbrauchEqualOrAfter(entry.getKey().getTyp(),
					getMonatsInt(new Date())) != null)
				throw new TransactionalException("already saved error", null);

			ret = saveStand(entry.getKey(), entry.getValue(), now);
			if (!ret) {
				throw new TransactionalException("save error", null);
			}
		}
		return ret;
	}

	@Transactional
	public boolean saveStand(final Medium medium, final BigDecimal zaehlerstand, final Date einreichungsdatum) {
		int typ = medium.getTyp();
		Zaehlerstand stand = new Zaehlerstand();
		Zaehler zaehler = zRepository.findOneByTyp(typ);
		stand.setZaehler(zaehler);
		stand.setDateneinreichung(einreichungsdatum);
		stand.setCreatedAt(new Date());
		stand.setUpdatedAt(new Date());
		stand.setStand(zaehlerstand);
		stand.setTyp(zaehler.getTyp());
		zsRepository.save(stand);
		return true;
	}

	public void setSettingsMap(String user, Map<String, Object> settings) {
		JSONObject obj = new JSONObject(settings);
		String rawSettings = obj.toString();
		Settings settingsEntity = settingsRepository.findOne(ZaehlerstaendeBackend.getCurrentUserName());
		Date d = new Date();
		if (settingsEntity == null) {
			settingsEntity = new Settings();
			settingsEntity.setBenutzer(user);
			settingsEntity.setCreatedAt(d);
		}
		settingsEntity.setSettings(rawSettings);
		settingsEntity.setUpdatedAt(d);
		settingsRepository.save(settingsEntity);
	}

	private Date setTimeToNull(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		return cal.getTime();
	}

	public String listAllVerbraeucheAsCSV() {

		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.GERMAN));

		List<Zaehlerverbrauch> allData = listAllVerbrauchsdaten();
		List<Triple<String, String, BigDecimal>> ret = new ArrayList<>(10);
		for (Zaehlerverbrauch zvb : allData) {
			ret.add(new MutableTriple<String, String, BigDecimal>(zvb.getMedium(), zvb.getAktMonatTechnisch(),
					zvb.getVerbrauch()));
		}
		StringBuffer retStr = new StringBuffer();
		retStr.append("\"Medium\"; \"Monat\"; \"Verbrauch\"\n");
		for (Triple<String, String, BigDecimal> triple : ret) {
			retStr.append("\"").append(triple.getLeft()).append("\";").append(triple.getMiddle()).append(';')
					.append(df.format(triple.getRight())).append('\n');
		}
		return retStr.toString();
	}
}
