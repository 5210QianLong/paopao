import myAxios from "../plugins/myAxios.js";

const getCurrentUser =  ( async ()=>{
    const res = await myAxios.get("/user/current")
    if (res.code === 0 ){
        res.date.gender = res.date.gender === 0?"男":"女"
        res.date.tags = JSON.parse(res.date.tags)
    }
    return res.date
})
export default getCurrentUser