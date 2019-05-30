package com.mycompany.control;

import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Agencia;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class ServiceImportAgencia {

    @SpringBean(name = "genericDao")
    private GenericDao<Agencia> genericDao;



    public void lerArqXlsAgencia(File file) {

        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            POIFSFileSystem fileSystem = new POIFSFileSystem(buf);
            HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
            HSSFSheet sheet = workbook.getSheetAt(0);

            // Capturando as linhas
            Iterator linhas = sheet.rowIterator();
            while (linhas.hasNext()) {
                HSSFRow linha = (HSSFRow) linhas.next();
                Iterator celulas = linha.cellIterator();

                Agencia agencia = new Agencia();

                while (celulas.hasNext()) {
                    HSSFCell celula = (HSSFCell) celulas.next();
                    int z = celula.getColumnIndex();

                    switch (z) {

                        case 0:
                            agencia.setUF(celula.toString());
                        case 1:
                            agencia.setCidade(celula.toString());
                        case 2:
                            agencia.setNumero(celula.toString());
                    }
                    genericDao.inserir(agencia);
                }
            }
        } catch (Exception e) {
           System.out.println("Erro: na hora de abrir dos analalistas no formato XLS");
        }
    }

    public void setGenericDao(GenericDao<Agencia> genericDao) {
        this.genericDao = genericDao;
    }
}
