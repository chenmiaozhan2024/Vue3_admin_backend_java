package com.itheima.java_admin_backend.common;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;      // 响应码，200 代表成功; 500 代表失败
    private String message;    // 响应信息 描述字符串
    private Object data;       // 返回的数据
    private Boolean ok;        // 是否成功

    //增删改 成功响应
    public static Result success(){
        return new Result(200,"success",null,true);
    }
    //查询 成功响应
    public static Result success(Object data){
        return new Result(200,"success",data,true);
    }
    //失败响应
    public static Result error(String message){
        return new Result(500,message,null,false);
    }
}
