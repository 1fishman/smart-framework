package org.smart.framework.dao.bean;

import org.smart.framework.core.bean.BaseBean;

import java.util.List;

public class Pager<T> extends BaseBean {

    private static final long serialVersionUID = 4423502157612253594L;

    private int pageNumber; //页面编号

    private int pageSize; //每页条数

    private long totalRecord; //总记录数

    private long totalPage; //总页面数

    private List<T> recodList; //数据列表

    public Pager(int pageNumber,int pageSize,long totalRecord,List<T> recodList){
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
        this.recodList = recodList;
        if(pageSize != 0){
            totalPage = totalRecord%pageSize == 0? totalRecord/pageSize:totalRecord/pageSize+1 ;
        }
    }


    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public List<T> getRecodList() {
        return recodList;
    }

    public boolean isFirstPage(){
        return pageNumber==1;
    }

    public boolean isLastPage(){
        return pageNumber==totalPage;
    }

    public boolean hasPrevPage(){
        return pageNumber > 1 && pageNumber <=totalPage;
    }

    public boolean hasNextPage(){
        return pageNumber < totalPage;
    }


}
