import {UserType} from "./user";

/**
 * 用户类别
 */
export type TeamType = {
    id:number;
    teamName: string,
    description: string,
    maxNum: number,
    expireTime: Date,
    userId: number,
    leaderId:number,
    status:number,
    password?:string,
    createTime: Date
    updateTime: Date,
    createUser?:UserType
}