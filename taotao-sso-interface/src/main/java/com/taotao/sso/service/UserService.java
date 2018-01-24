package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;


/**
 * @author 第二十五颗星星
 * @create 2018-01-23 18:58
 * @desc 用户service
 **/
public interface UserService {
    TaotaoResult checkData(String parma,int type);
    TaotaoResult createUser(TbUser tbUser);
    TaotaoResult login(String username,String password);
}
