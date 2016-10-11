package xd.fw.bean.mapper;

import org.apache.ibatis.annotations.Param;

public interface JknUserMapper {

    int userWithdrawCount(@Param("userId") int userId, @Param("totalCount") int totalCount);

    Integer sumFeeFromGeneration(@Param("generation") int generation,
                        @Param("userId") int userId);

    int deleteEarlierEvent();

}
