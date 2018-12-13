package xin.yuki.auth.core.base;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

@RegisterMapper
public interface BaseListMapper<T> extends Mapper<T>, InsertListMapper<T> {

}
