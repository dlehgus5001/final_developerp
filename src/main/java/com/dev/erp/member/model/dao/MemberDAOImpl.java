package com.dev.erp.member.model.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dev.erp.member.model.vo.Member;

@Repository
public class MemberDAOImpl implements MemberDAO {

	@Autowired
	SqlSessionTemplate sqlSession;

	@Override
	public int insertMember(Member member) {
		return sqlSession.insert("member.insertMember",member);
	}

	@Override
	public List<Map<String, String>> selectDeptList() {
		return sqlSession.selectList("member.selectDeptList");
	}

	@Override
	public List<Map<String, String>> selectJobList() {
		return sqlSession.selectList("member.selectJobList");
	}

	@Override
	public List<Member> memberSelectList() {
		return sqlSession.selectList("member.memberSelectList");
	}

	@Override
	public Member selectOneMember(String email) {
		return sqlSession.selectOne("member.selectOneMember",email);
	}
}