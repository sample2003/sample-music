import accessEnum from "@/util/access/accessEnum";

const state = {
    userToken: '',
    userDetail: {
        id: 0,
        nickname: accessEnum.NOT_LOGIN,
        username: accessEnum.NOT_LOGIN,
        phone: "",
        email: "",
        sex: "",
        description: "",
        exp: 0,
        idNumber: "",
        avatar: "",
        likes: 0,
        status: 0,
        createTime: "",
        updateTime: "",
    }
}

export default state;