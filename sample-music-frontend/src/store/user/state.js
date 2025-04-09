import accessEnum from "@/util/access/accessEnum";

const state = {
    userToken: '',
    userDetail: {
        id: 0,
        username: accessEnum.NOT_LOGIN,
        phone: "",
        email: "",
        sex: "",
        idNumber: "",
        avatar: "",
        likes: 0,
        status: 0,
        createTime: "",
        updateTime: "",
    }
}

export default state;