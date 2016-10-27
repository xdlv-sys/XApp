package xd.dl.bean.mapper;

import org.apache.ibatis.annotations.Param;
import xd.fw.bean.EnterOrOutRecord;

import java.util.List;

public interface EnterOrOutRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hjc_enterorout_record
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String orderNum);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hjc_enterorout_record
     *
     * @mbggenerated
     */
    int insert(EnterOrOutRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hjc_enterorout_record
     *
     * @mbggenerated
     */
    int insertSelective(EnterOrOutRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hjc_enterorout_record
     *
     * @mbggenerated
     */
    EnterOrOutRecord selectByPrimaryKey(String orderNum);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hjc_enterorout_record
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(EnterOrOutRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hjc_enterorout_record
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(EnterOrOutRecord record);

    List<EnterOrOutRecord> selectUploadRecords(@Param("enterOrOut")int enterOrOut);

    void updateRecordStatus(@Param("orderNum")String orderNum
            , @Param("status")int retStatus, @Param("msg")String msg);

    int getFreeParkStation();

    int deleteFinishRecords();
}