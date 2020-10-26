package pe.csti.capa.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.csti.capa.domain.Ejemplo;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/welcome")
public class WelcomeRest {

    private final Logger log = LoggerFactory.getLogger(WelcomeRest.class);

    @RequestMapping(value = "/fechaActual", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> fechaActual() throws URISyntaxException {
        log.error("Find by fechaActual");
        try {
            Date fecha = this.obtenerFechaHoraActual();
            String sFecha = this.convertDateToString("dd/MM/yyyy HH:mm:ss", fecha);
            return Optional.ofNullable(sFecha).map(x -> new ResponseEntity<>(x, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            HttpHeaders headers = this.devuelveErrorHeaders(e);
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value = "/fechaActualMejorado", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> fechaActualMejorado() throws URISyntaxException {
        log.error("Find by fechaActualMejorado");
        try {
            Date fecha = this.obtenerFechaHoraActual();
            String sFecha = this.convertDateToString("dd/MM/yyyy HH:mm:ss", fecha);
            return Optional.ofNullable(sFecha).map(x -> new ResponseEntity<>(x, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            HttpHeaders headers = this.devuelveErrorHeaders(e);
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/devuelveNombres", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Ejemplo> devuelveNombres(@RequestBody Ejemplo ejemplo) throws URISyntaxException {
        log.error("Find by devuelveNombres");
        try {
            ejemplo.setApellidosNombres(ejemplo.getApellidos() + " - " + ejemplo.getNombres());
            return Optional.ofNullable(ejemplo).map(x -> new ResponseEntity<>(x, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            HttpHeaders headers = this.devuelveErrorHeaders(e);
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
    }

    private Date obtenerFechaHoraActual() {
        ZoneId zona = ZoneId.systemDefault();
        //Date fechaActual =  Date.from(LocalDateTime.now().atZone(zona).toInstant());

        Instant instant = Instant.now() ;  // Capture current moment in UTC.
        ZoneId zLima = ZoneId.of( "America/Lima" ) ;
        ZonedDateTime zdtLima = instant.atZone( zLima );
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formateo = zdtLima.format(fmt);
        Date fechaActual = Date.from(LocalDate.now().atStartOfDay(zona).toInstant());
        try {
            fechaActual = convertStringToDate("dd/MM/yyyy HH:mm:ss", formateo);
        }
        catch(Exception e) {

        }
        return fechaActual;
    }

    public Date convertStringToDate(String aMask, String strDate)
            throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        if (log.isDebugEnabled()) {
            log.debug("converting '" + strDate + "' to date with mask '"
                    + aMask + "'");
        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            //log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }
        return (date);
    }


    private String convertDateToString(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    private HttpHeaders devuelveErrorHeaders(Exception e) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("errors", e.getMessage());
        return headers;
    }



}


