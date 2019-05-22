package com.mycompany.relatorios;

import com.mycompany.model.Colaborador;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.wicket.util.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class RelatorioExcelColaborador implements Serializable {

    public static void main(String[] args) {

    }

    public byte[] criarExcel(List<Colaborador> lista) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Colaboradores");
        int rownum = 1;
        Row row0 = sheet.createRow(0);


        Cell cell1 = row0.createCell(0);
        cell1.setCellValue("Nome");
        Cell cell2 = row0.createCell(1);
        cell2.setCellValue("CPF");
        Cell cell3 = row0.createCell(2);
        cell3.setCellValue("Data de Nascimento");
        Cell cell4 = row0.createCell(3);
        cell4.setCellValue("Agência");


        for (Colaborador colaboradorDaLista : lista) {
            Row row = sheet.createRow(rownum++);
            int cellnum = 0;

            Cell cell1Valor = row.createCell(cellnum++);
            cell1Valor.setCellValue(colaboradorDaLista.getNome());
            Cell cell2Valor = row.createCell(cellnum++);
            cell2Valor.setCellValue(colaboradorDaLista.getCpf());
            Cell cell3Valor = row.createCell(cellnum++);
            cell3Valor.setCellValue(colaboradorDaLista.getDataDeNascimento());
            Cell cell4Valor = row.createCell(cellnum++);
            cell4Valor.setCellValue(colaboradorDaLista.getAgencia().getNumero());

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
        }
        ByteArrayOutputStream out =
                new ByteArrayOutputStream();
        workbook.write(out);
        out.close();
        return out.toByteArray();
    }

}
