package com.dev.erp.document.model.service;

import java.util.List;
import java.util.Map;

import com.dev.erp.document.model.vo.Document;
import com.dev.erp.document.model.vo.DocumentLine;

public interface DocumentService {

	List<Document> selectDocList(String empName);

	List<Map<String, String>> selectDocTypeAll();

	int insertDocument(Document document);

	int insertDocumentLine(DocumentLine documentLine);

	List<Map<String, String>> selectMemberList(int cPage, int numPerPage);

	int selectAllCountByAccountNo();

	Document documentDetailView(int docNo);

	int approveDocument(int docNo);

	int refuseDocument(int docNo);


}
