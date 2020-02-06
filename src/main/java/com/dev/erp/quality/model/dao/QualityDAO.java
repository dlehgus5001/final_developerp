package com.dev.erp.quality.model.dao;

import java.util.List;
import java.util.Map;

import com.dev.erp.quality.model.vo.Quality;

public interface QualityDAO {

	List<Map<String, String>> selectQaulityAll();

	Quality selectOnebyQualityNo(int quality_no);

	int qualityUpdateOne(Quality quality);

	List<Map<String, String>> selectStoreAll();

	List<Map<String, String>> selectQualityColumn(String searchType);

	List<Map<String, String>> selectProductTypeAll();

}
