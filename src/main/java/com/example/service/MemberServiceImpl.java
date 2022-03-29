package com.example.service;

import java.util.List;

import com.example.dto.MemberDTO;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    // 호출하는 곳
    // xml로 되어있는 mapper를 호출함.
    @Autowired
    SqlSessionFactory sqlFactory;

    @Override
    public int insertMember(MemberDTO member) {
        // namespace가 Member이고 id가 insertMemberOne인 항목을 호출함.
        return sqlFactory.openSession().insert("Member.insertMemberOne", member);
    }

    @Override
    public List<MemberDTO> selectMemberList() {
        // namespace명, id명으로 호출
        return sqlFactory.openSession().selectList("Member.selectMemberList");
    }

    @Override
    public int deleteMemberOne(String uemail) {
        return sqlFactory.openSession().delete("Member.deleteMemberOne", uemail);
    }

    @Override
    public MemberDTO selectMemberOne(String uemail) {
        return sqlFactory.openSession().selectOne("Member.selectMemberOne", uemail);
    }

    @Override
    public int updateMemberOne(MemberDTO member) {
        return sqlFactory.openSession().update("Member.updateMemberOne", member);
    }

    @Override
    public MemberDTO selectMemberLogin(MemberDTO member) {
        // xml mapper 호출 Member id가 selectMemberLogin인 것
        return sqlFactory.openSession().selectOne("Member.selectMemberLogin", member);
    }

}
