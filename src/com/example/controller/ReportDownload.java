package com.example.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.dao.PersonDao;
import com.example.model.Person;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@WebServlet("/ReportDownload")
public class ReportDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 前端傳進來的參數
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String reportType = request.getParameter("reportType");
		
		File file = null;
		JasperReport jasperReport = null;
		JRDataSource dataSource = null;
		JRAbstractExporter exporter = null;
		byte[] bytes = null;
		
		try {
			String path = "/Users/greg/Desktop/JasperReportExample.jasper";
			file = new File(path);
			jasperReport = (JasperReport) JRLoader.loadObject(file);
		} catch(JRException e) {
			System.err.println(e);
		}
		
		// 資料庫查詢資料
		PersonDao dao = new PersonDao();
		List<Person> personList = dao.getPersonList(startDate, endDate+" 23:59:59");

		int size = personList.size();
		if(size > 0) {
			dataSource = new JRBeanCollectionDataSource(personList);
		}else {
			dataSource = new JREmptyDataSource();
		}
		
		Map parameters=new HashMap();
		parameters.put ("searchDate", startDate + "~" + endDate);
			
		if("PDF".equals(reportType)) {
			exporter = new JRPdfExporter();
		}else if ("Excel".equals(reportType)) {
			exporter = new JRXlsExporter();
		}
		
		try {
			ByteArrayOutputStream oStream = new ByteArrayOutputStream();
			JasperPrint jasperPrint = JasperFillManager.fillReport("/Users/greg/Desktop/JasperReportExample.jasper", parameters, dataSource);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
			exporter.exportReport();
			bytes = oStream.toByteArray();
			oStream.close();
			
		} catch (JRException e) {
			e.printStackTrace();
			System.err.println(e);
		}
		
        if("PDF".equals(reportType)) {
        	response.setHeader("Content-Disposition","attachment; filename=JasperReportExample.pdf");
            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
		}else if("Excel".equals(reportType)) {
			response.setHeader("Content-Disposition","attachment; filename=JasperReportExample.xls");
			response.setContentType("application/vnd.ms-excel");
			response.setContentLength(bytes.length);
		}
        
        ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);
		ouputStream.flush();
		ouputStream.close();
        
	}

}
