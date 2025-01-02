package br.com.erudio.file.exporter.contract;

import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.file.exporter.FileExportResponse;

import java.util.List;

public interface FileExporter {

    FileExportResponse exportFile(List<PersonDTO> people) throws Exception;

}
