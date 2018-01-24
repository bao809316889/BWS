package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserQuery;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 第二十五颗星星
 * @create 2018-01-23 18:59
 * @desc 用户service实现类
 **/
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    private TbUserMapper tbUserMapper;

    @Value("USER_INFO")
    private String USER_INFO;

    @Value("SESSION_EXPIRE")
    private int SESSION_EXPIRE;

    @Resource
    private JedisClient jedisClient;
    @Override
    public TaotaoResult checkData(String parma, int type) {
        //从tbuser表中查数据
        TbUserQuery query = new TbUserQuery();
        TbUserQuery.Criteria criteria = query.createCriteria();
        //根据查询条件拼接
        //1、2、3分别代表username、phone、email
        if (type == 1) {
            criteria.andUsernameEqualTo(parma);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(parma);
        } else if (type == 3) {
            criteria.andEmailEqualTo(parma);
        } else {
            return TaotaoResult.build(400, "非法的参数");
        }
        List<TbUser> list = tbUserMapper.selectByExample(query);
        //判断list集合是否为空
        if (list == null || list.size() == 0) {
            return TaotaoResult.ok(true);
        }
        return TaotaoResult.ok(false);
    }

    @Override
    public TaotaoResult createUser(TbUser tbUser) {
        //校验用户输入的数据
        if (StringUtils.isBlank(tbUser.getUsername())) {
            return TaotaoResult.build(400, "用户名不能为空");
        }
        if (StringUtils.isBlank(tbUser.getPassword())) {
            return TaotaoResult.build(400, "密码不能为空");
        }
        if (StringUtils.isBlank(tbUser.getPhone())) {
            return TaotaoResult.build(400, "手机号不能为空");
        }
        TaotaoResult usernameResult = checkData(tbUser.getUsername(), 1);
        if (!(boolean) usernameResult.getData()) {
            return TaotaoResult.build(400, "该用户名已经被占用");
        }
        TaotaoResult phoneResult = checkData(tbUser.getPhone(), 2);
        if (!(boolean) phoneResult.getData()) {
            return TaotaoResult.build(400, "该手机号已经被占用");
        }
        TaotaoResult emailResult = checkData(tbUser.getEmail(), 3);
        if (!(boolean) emailResult.getData()) {
            return TaotaoResult.build(400, "该邮箱已经被占用");
        }
        //补全user属性
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        //对密码进行MD5加密
        String password = DigestUtils.md5DigestAsHex(tbUser.getUsername().getBytes());
        tbUser.setPassword(password);
        //将用户数据插入数据库
        tbUserMapper.insert(tbUser);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult login(String username, String password) {
        //判断用户名密码是否正确。
        TbUserQuery query = new TbUserQuery();
        TbUserQuery.Criteria criteria = query.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> Users = tbUserMapper.selectByExample(query);
        if (Users == null || Users.size() == 0) {
            return TaotaoResult.build(400,"您输入的用户名有误");
        }
        //查询用户信息
        TbUser user = Users.get(0);
        //校验密码
        if (!password.equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))){
            return TaotaoResult.build(400,"您输入的密码不正确");
        }
        //使用uuid生成token,存入redis中,token为key,user的json串为value
        String token = UUID.randomUUID().toString();
        //安全起见把user密码置为空
        user.setPassword(null);
        //将用户信息存入jedis中
        jedisClient.set(USER_INFO+":"+token, JsonUtils.objectToJson(user));
        //设置redis过期时间
        jedisClient.expire(USER_INFO+":"+token,SESSION_EXPIRE);
        return TaotaoResult.ok(user);
    }
}