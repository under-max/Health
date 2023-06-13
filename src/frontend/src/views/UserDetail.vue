<template>
  <hr>
  <hr>
  <div class="row justify-content-left">
    <div class="col-12 col-md-8 col-lg-4">
      <h1>{{ user.name }}님의 프로필</h1>
      <div class="mb-3">
        <div class="mb-3">
          <label for="" class="form-label">이름</label>
          <input type="text" class="form-control" :value="user.name" id="inputUserName" readonly >
        </div>
        <div class="mb-3">
          <label for="" class="form-label">센터 이름</label>
          <input type="text" class="form-control" :value="user.centerName" id="inputCenterName" readonly >
        </div>
        <div class="mb-3">
          <label for="" class="form-label">센터 주소</label>
          <input type="text" class="form-control" :value="user.centerAddress" id="inputCenterAddress" readonly >
        </div>
        <div class="mb-3">
          <label for="" class="form-label">담당 트레이너</label>
          <input type="text" class="form-control" :value="user.trainerName" id="inputTrainerName" readonly >
        </div>
        <div class="mb-3">
          <label for="" class="form-label">이용권 기간</label>
          <input type="text" class="form-control" :value="periodMembership" id="inputPeriodMembership" readonly >
        </div>
        <div class="mb-3">
          <label for="" class="form-label">남은 PT 횟수</label>
          <input type="text" class="form-control" :value="user.remainingPT" id="inputRemainingPT" readonly >
        </div>

      </div>
    </div>
  </div>

</template>

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

const periodMembership = ref();


const user = ref({
  id: '',
  name: '',
  email: '',
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
  axios.get(`/api/user/get/${props.userId}`, {
    headers: {
      Authorization: token
    }
  })
      .then((response) => {
        console.log(response.data)
        user.value = response.data;
        periodMembership.value = `${user.value.startDate} ~ ${user.value.endDate}`
        console.log(response.data.startDate)
        console.log(response.data.endDate)
      }).catch((error) => {
    if (error.response) {
      alert(error.response.data.message);
    }
  });
});

</script>



<style scoped>
h1 {
  color: white;
}

label {
  color: white;
}
</style>
