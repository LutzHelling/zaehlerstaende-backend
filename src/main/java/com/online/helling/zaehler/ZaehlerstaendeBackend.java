package com.online.helling.zaehler;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ImportResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.online.helling.zaehler.dataaccess.ZaehlerAccess;
import com.online.helling.zaehler.dataaccess.model.Jahresstand;
import com.online.helling.zaehler.dataaccess.model.Zaehler;
import com.online.helling.zaehler.dataaccess.model.ZaehlerInfo;
import com.online.helling.zaehler.dataaccess.model.Zaehlerverbrauch;
import com.online.helling.zaehler.dataaccess.model.pixometer.Reading;
import com.online.helling.zaehler.jwt.JwtRequest;
import com.online.helling.zaehler.jwt.JwtResponse;
import com.online.helling.zaehler.jwt.JwtTokenUtil;
import com.online.helling.zaehler.util.ChartStand;
import com.online.helling.zaehler.util.Medium;
import com.online.helling.zaehler.util.Zeitreihe;

@EnableScheduling
@SpringBootApplication
@ImportResource({ "classpath:META-INF/applicationContext.xml" })
@RestController
public class ZaehlerstaendeBackend {

	@Autowired(required = true)
	private ZaehlerAccess zaehlerAccess;

	private static final DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private static final DecimalFormat df = new DecimalFormat("0.000");

	static {
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.GERMAN));
	}

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	static ResourceBundle bundle = ResourceBundle.getBundle("config/application");

	@RequestMapping(value = "/auth", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<?> generateAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	public static String getCurrentUserName() {
		String currentUserName = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			currentUserName = authentication.getName();
		}
		if (currentUserName.length() == 0)
			throw new IllegalArgumentException("User not authenticated");
		return currentUserName;
	}

	public static void main(String[] args) {
		SpringApplicationBuilder app = new SpringApplicationBuilder().sources(ZaehlerstaendeBackend.class);
		app.run(args);
	}

	@RequestMapping(path = "/getZaehlerInfo", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String getZaehlerInfo(@RequestParam("medium") int medium) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.setPrettyPrinting().setDateFormat("dd.MM.yyyy").create();
		ZaehlerInfo ret = new ZaehlerInfo();
		Medium medi = Medium.fromInt(medium);
		Zaehler zaehler = zaehlerAccess.getZaehler(medi);
		Zaehlerverbrauch ls = zaehlerAccess.getLastStand(medi);
		String nummer = zaehler.getZaehlerId();
		ret.medium = medium;
		ret.bezeichnung = medi.toString();
		ret.nummer = nummer;
		ret.letzteAblesung = ls.getAktDate();
		ret.letzterStand = getLastStand(medium);
		ret.einheit = Medium.getEinheit(medi);
		String json = gson.toJson(ret);
		System.out.println("Response: " + json);
		return json;
	}

	@RequestMapping(path = "/getAllStaendeAsCSV", produces = MediaType.TEXT_PLAIN_VALUE, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> getAlleStaendeAsCSV() {

		List<Zaehlerverbrauch> allData = zaehlerAccess.listAllVerbrauchsdaten();
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

		HttpHeaders header = new HttpHeaders();
		header.setContentLength(retStr.length());
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AlleVerbraeuche.xlsx");
		header.setContentType(new MediaType("application", "vnd.ms-excel"));
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");

		return new ResponseEntity<String>(retStr.toString(), header, HttpStatus.OK);
	}

	@RequestMapping(path = "/getAllStaende", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String getAllStaende(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
			@RequestParam(name = "medium") Optional<Integer> onlyMedium) {
		EnumMap<Medium, Zeitreihe<BigDecimal>> allStaende = zaehlerAccess.getJahresstaende(start);
		List<ChartStand<Date>> ret = new ArrayList<>(allStaende.size());
		for (Medium medium : allStaende.keySet()) {
			if ((onlyMedium.isEmpty()) || (onlyMedium.isPresent() && medium.getTyp() == onlyMedium.get())) {
				Zeitreihe<BigDecimal> zr = allStaende.get(medium);
				zr.forEach(new Consumer<Pair<Date, BigDecimal>>() {
					@Override
					public void accept(Pair<Date, BigDecimal> elem) {
						ChartStand<Date> tmp = new ChartStand<>(medium, elem.getLeft(), elem.getRight());
						ret.add(tmp);
					}
				});
			}
		}

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ChartStand.class, new ChartStandSerializer()).setPrettyPrinting();
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(ret);
		return json;
	}

	public class ChartStandSerializer implements JsonSerializer<ChartStand<Date>> {
		@Override
		public JsonElement serialize(ChartStand src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("medium", src.getMedium().getTyp());
			if (src.getTime() instanceof Date)
				jsonObject.addProperty("stand", sdf.format(src.getTime()));
			else
				jsonObject.addProperty("stand", (int) src.getTime());
			jsonObject.addProperty("wert", src.getWert().toString());
			return jsonObject;
		}
	}

	class CustomStandSerializer<E> implements JsonSerializer<Pair<Date, E>> {
		@Override
		public JsonElement serialize(Pair<Date, E> stand, Type type,
				JsonSerializationContext jsonSerializationContext) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("stand", sdf.format(stand.getLeft()));
			jsonObject.addProperty("wert", df.format(stand.getRight()));
			return jsonObject;
		}

	}

	@RequestMapping(value = "/saveStand", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveStand(@RequestParam("medium") int medium,
			@RequestParam("stand") BigDecimal zaehlerstand) {
		if (zaehlerAccess.saveStand(Medium.fromInt(medium), zaehlerstand, new Date()))
			return "OK";
		else
			return "ERROR";
	}

	@RequestMapping(value = "/getLastStand", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BigDecimal getLastStand(@RequestParam("medium") int medium) {
		Zaehlerverbrauch lastEntry = zaehlerAccess.getLastStand(Medium.fromInt(medium));
		if (lastEntry == null) {
			return null;
		} else {
			return lastEntry.getZaehlerstand();
		}
	}

	@RequestMapping("/isAlive")
	@ResponseBody
	public String isAlive() {
		return "OK";
	}

	public static String getTokenPrefix() {
		return bundle.getString("token.prefix");
	}

	public static String getHeaderString() {
		return bundle.getString("header.string");
	}

	@RequestMapping(value = "/getJahresstaende", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getJahresstaende(@RequestParam("medium") int medium) {
		List<Jahresstand> ret = zaehlerAccess.listJahresstaende(Medium.fromInt(medium));
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(ret);
		return json;
	}

	@RequestMapping(value = "/getAllJahresstaende", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAllJahresstaende(@RequestParam("medium") Optional<Integer> medium) {

		List<Jahresstand> temp = new ArrayList<>();
		List<ChartStand<Integer>> ret = new ArrayList<>(temp.size());
		
		if (medium.isEmpty())
			for (Medium curMedium : Medium.values()) {
				temp.addAll(zaehlerAccess.listJahresstaende(curMedium));
			}
		else
			temp.addAll(zaehlerAccess.listJahresstaende(Medium.fromInt(medium.get())));
		
		temp.forEach(new Consumer<Jahresstand>() {
			@Override
			public void accept(Jahresstand t) {
				ChartStand<Integer> tmp = new ChartStand<>(Medium.fromInt(t.id.typ), t.id.jahr, t.verbrauch);
				ret.add(tmp);
			}
		});

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ChartStand.class, new ChartStandSerializer()).setPrettyPrinting();
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(ret);
		return json;

	}

	@RequestMapping(value = "/saveStandJSON", method = { RequestMethod.POST })
	public @ResponseBody String saveStandJSON(@RequestParam("medium") int medium,
			@NumberFormat(pattern = "#0.00") @RequestParam("zaehlerstand") BigDecimal zaehlerstand,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @JsonDeserialize(using = LocalDateTimeDeserializer.class) @RequestParam("einreichungsdatum") Date einreichungsdatum) {

		boolean ret = zaehlerAccess.saveStand(Medium.fromInt(medium), zaehlerstand, einreichungsdatum);
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(new ImmutablePair<String, Boolean>("OK", ret));
		return json;
	}

	@RequestMapping(value = "/importAllPixo", method = { RequestMethod.POST })
	public @ResponseBody String importAllPixo() {

		String msg = "OK";
		int code = 200;
		PixometerClient pixo = new PixometerClient();
		for (Medium medium : Medium.values()) {
			BigDecimal imported = null;
			try {
				imported = importData(pixo, medium);
			} catch (HttpClientErrorException.Unauthorized ex) {
				msg = "Zugriff verboten";
				code = 403;
			}

			if (imported == null) {
				msg = "keine neuen Daten gefunden";
				code = 417;
			} else {
				try {
					saveStandNow(medium, imported);
				} catch (Exception e) {
					e.printStackTrace();
					msg = "Fehler beim Speichern";
					code = 400;
				}
			}
		}
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		JsonObject resp = new JsonObject();
		resp.addProperty("message", msg);
		resp.addProperty("status", code);
		String json = gson.toJson(resp);
		return json;
	}

	public BigDecimal importData(PixometerClient pixo, Medium medium) {
		Zaehlerverbrauch lastStand = zaehlerAccess.getLastStand(medium);
		Calendar cal = GregorianCalendar.getInstance();
		{
			cal.setTime(lastStand.getAktDate());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			String zaehlerstand = pixo.getReadings(medium.getTyp(), cal.getTime());
			Gson gson = new Gson();
			Reading reading = gson.fromJson(zaehlerstand, Reading.class);
			if (reading.getCount() > 0) {
				return new BigDecimal(reading.getResults().get(0).getValue());
			} else {
				return null;
			}
		}
	}

	private void saveStandNow(Medium medium, BigDecimal verbrauch) throws Exception {
		Date zeitpunkt = new Date();
		if (!zaehlerAccess.saveStand(medium, verbrauch, zeitpunkt)) {
			throw new Exception("Fehler beim Speichern");
		}
	}

}
