package example.micronaut;

import example.micronaut.error.InvalidFileException;
import io.micronaut.http.MediaType;
import io.micronaut.http.multipart.CompletedFileUpload;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class ExcelFileProcessor {
    private static final MediaType EXCEL_MEDIA_TYPE = MediaType.of("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    public long countRows(CompletedFileUpload file){
        MediaType fileContentType =
                file.getContentType().orElseThrow(() -> new InvalidFileException("The file has no content-type"));
        if(!fileContentType.equals(EXCEL_MEDIA_TYPE)){
            throw new InvalidFileException("The file content-type is not supported: " + fileContentType);
        }
        return getWorkbook(file).getSheetAt(0).getPhysicalNumberOfRows();
    }

    private XSSFWorkbook getWorkbook(CompletedFileUpload file) {
        try {
            return new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            throw new InvalidFileException("Cannot read the file");
        }
    }
}
