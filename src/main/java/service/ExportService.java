package service;

import java.util.List;

public interface ExportService<T> {
    void export(List<T> data, String filePath) throws Exception;
}
