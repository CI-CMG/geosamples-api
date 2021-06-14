package noaa.ncei.ogssd.geosamples.web

import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.Hidden
import noaa.ncei.ogssd.geosamples.DemoRepository
import noaa.ncei.ogssd.geosamples.GeosamplesDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import sun.misc.Request

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@Slf4j
@RestController
class SandboxController {
    @Autowired
    DemoRepository demoRepository

    @CrossOrigin
    @GetMapping("/sandbox")
    def sandbox(HttpServletRequest request, HttpServletResponse response) {
        println('inside sandbox...')
        demoRepository.getRecords()
        demoRepository.databaseSpecificMethod()
    }

    @Hidden
    @GetMapping("/testme")
    def testme(
            @RequestParam(defaultValue = "false", value = "count_only") boolean countOnly,
            @RequestParam(defaultValue = "false", value = "full_record") boolean fullRecord,
            @Valid GeosamplesDTO searchParams
    ) {
        println(searchParams.whereClause)
        println(searchParams.criteriaValues)
        println("countOnly: ${countOnly}")
        return []

    }

    @GetMapping("/export")
    def export(@RequestParam(defaultValue="json") String format,  HttpServletResponse response) {
        if (format == 'csv') {
            // TODO
        } else {
            return [1,2,3,4]
        }
    }

    def exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//        String currentDateTime = dateFormatter.format(new Date());
        String currentDateTime = '-now'
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=geosamples" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);


//        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
//        String[] csvHeader = {"User ID", "E-mail", "Full Name", "Roles", "Enabled"};
//        String[] nameMapping = {"id", "email", "fullName", "roles", "enabled"};
//
//        csvWriter.writeHeader(csvHeader);
//
//        for (User user : listUsers) {
//            csvWriter.write(user, nameMapping);
//        }
//
//        csvWriter.close();
    }

}