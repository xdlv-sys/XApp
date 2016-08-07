package xd.fw.bean.mapper;

import org.apache.ibatis.annotations.Param;

public interface JknUserMapper {

    int modifyUserCount(@Param("userId") int userId, @Param("count") int count,
                        @Param("countOne") int countOne,
                        @Param("countTwo") int countTwo,
                        @Param("countThree") int countThree);
}
