package com.dev.erp.quality.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dev.erp.quality.model.vo.Quality;

@Repository
public class QualityDAOImpl implements QualityDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<Map<String, String>> selectQaulityAll() {
		
		return sqlSession.selectList("quality.selectQualityAll");
	}

	@Override
	public Quality selectOnebyQualityNo(int quality_no) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("quality.selectOnebyQualityNo", quality_no);
	}

	@Override
	public int qualityUpdateOne(Quality quality) {
		// TODO Auto-generated method stub
		return sqlSession.update("quality.qualityUpdateOne",quality);
	}

	@Override
	public List<Map<String, String>> selectStoreAll(int cPage, int numPerPage) {
		// TODO Auto-generated method stub
		RowBounds rowBounds = new RowBounds((cPage-1)*numPerPage, numPerPage);
		return sqlSession.selectList("quality.selectStoreAll",null,rowBounds);
	}

	@Override
	public List<Map<String, String>> selectQualityColumn(String searchType, int cPage, int numPerPage) {
		// TODO Auto-generated method stub
		RowBounds rowBounds = new RowBounds((cPage-1)*numPerPage, numPerPage);
		return sqlSession.selectList("quality.selectQualityColumn", searchType, rowBounds);
	}

	@Override
	public List<Map<String, String>> selectProductTypeAll() {
		// TODO Auto-generated method stub
		return sqlSession.selectList("quality.selectProductTypeAll");
	}

	@Override
	public int selectAllCountByStoreNo() {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("quality.selectAllCountByStoreNo");
	}

	@Override
	public int selectAllCountForQuality() {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("quality.selectAllCountForQuality");
	}
	
	
}
