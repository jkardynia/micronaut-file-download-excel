package example.micronaut;

import example.micronaut.dto.CountRowsResponse;
import example.micronaut.dto.ErrorResponse;
import example.micronaut.error.InvalidFileException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.SystemFile;
import io.micronaut.views.View;

import java.util.HashMap;
import java.util.Map;

@Controller // <1>
public class HomeController {

    protected final BookRepository bookRepository;
    protected final BookExcelService bookExcelService;
    protected final ExcelFileProcessor excelFileProcessor;

    public HomeController(BookRepository bookRepository,  // <2>
                          BookExcelService bookExcelService,
                          ExcelFileProcessor excelFileProcessor) {
        this.bookRepository = bookRepository;
        this.bookExcelService = bookExcelService;
        this.excelFileProcessor = excelFileProcessor;
    }

    @View("index") // <3>
    @Get
    public Map<String, String> index() {
        return new HashMap<>();
    }

    @Produces(value = "application/vnd.ms-excel")
    @Get("/excel") // <4>
    public SystemFile excel() { // <5>
        return bookExcelService.excelFileFromBooks(bookRepository.findAll());
    }

    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = "application/json")
    @Post("/count-rows")
    public HttpResponse countRows(CompletedFileUpload file) {
        try {
            long rowsCount = excelFileProcessor.countRows(file);
            return HttpResponse.ok(new CountRowsResponse(rowsCount));
        }catch (InvalidFileException e){
            return HttpResponse.badRequest(new ErrorResponse(e.getMessage()));
        }
    }
}
