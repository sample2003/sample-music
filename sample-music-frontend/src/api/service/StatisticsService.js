import service from "@/api/request";

const StatisticsService = {
  recordListen: function (listenDetail) {
    return service({
      url: "/api/statistics/recordListen",
      method: "POST",
      data: listenDetail
    }).then(response => {
      return response.message;
    }).catch(error => {
      throw error;
    })
  },
}

export default StatisticsService;