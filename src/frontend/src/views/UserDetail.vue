<script setup>
import {defineProps, onMounted, ref} from "vue";
import axios from "axios";
import Cookies from "vue-cookies";

const props = defineProps({
  userId: {
    type: [Number, String],
    required: true
  },
});

const user = ref({
  id: '',
  name: '',
  email: '',
  phoneNumber: '',
  gender: '',
  age: '',
  isInCenter: '',
  trainerID: '',
  trainerName: '',
  centerName: '',
  startDate: '',
  endDate: '',
  remainingPT: ''

});

onMounted(() => {
  const token = Cookies.get("accessToken")
  console.log(props.userId)
  axios.get(`/api/test/user/${props.userId}`, {
    headers: {
      Authorization: token
    }
  })
      .then((response) => {
        console.log(response)
        user.value = response.data;
        console.log(response.data.startDate)
        console.log(response.data.endDate)
      }).catch((error) => {
    if (error.response) {
      alert(error.response.data.message);
    }
  });
});

</script>

<template>
  <hr>
  <h1>{{ user.name }}님의 프로필</h1>
  <div class="detail">
    <el-input type="hidden" class="detailId" :value="user.id" readonly/>
    이름: <label for="detailName">
    <el-input class="detailName" :value="user.name" readonly/>
  </label><br>
    이메일: <label for="detailEmail">
    <el-input class="detailEmail" :value="user.email" readonly/>
  </label><br>
    핸드폰 번호: <label for="detailPhone">
    <el-input class="detailPhone" :value="user.phoneNumber" readonly/>
  </label><br>
    성별: <label for="detailGender">
    <el-input class="detailGender" :value="user.gender" readonly/>
  </label><br>
    나이: <label for="detailAge">
    <el-input class="detailAge" :value="user.age" readonly/>
  </label><br>
    센터 출입 확인: <label for="detailIsInCenter">
    <el-input class="detailIsInCenter" :value="user.isInCenter" readonly/>
  </label><br>
    <el-input type="hidden" class="detailTrainerId" :value="user.trainerID" readonly/>
    트레이너 이름: <label for="detailTrainerName">
    <el-input class="detailTrainerName" :value="user.trainerName" readonly/>
  </label><br>
    센터 이름: <label for="detailCenter">
    <el-input class="detailCenter" :value="user.centerName" readonly/>
  </label><br>
    이용 기간: <label for="detailDate">
    <el-input type="text" class="detailStartDate" :value="user.startDate" readonly/>
    <el-input type="text" class="detailEndDate" :value="user.endDate" readonly/>
  </label><br>
    남은 PT 횟수: <label for="detailRemainingPT">
    <el-input class="detailRemainingPT" :value="user.remainingPT" readonly/>
  </label>
  </div>

</template>

<style scoped>
.detail {
  color: white;
}

label {
  color: white;
}
</style>
