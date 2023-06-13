<template>
  <hr>
  <div class="row justify-content-center">
    <div class="col-12 col-md-8 col-lg-6">
      <h1>트레이너 상세 페이지</h1>

      <div class="mb-3">
        <label for="" class="form-label">트레이너 이름</label>
        <input type="text" class="form-control" :value="trainer.name" id="inputTrainerName" readonly>
      </div>
      <div class="mb-3">
        <label for="" class="form-label">센터 이름</label>
        <input type="text" class="form-control" :value="trainer.centerName" id="inputCenterName" readonly>
      </div>
      <div class="mb-3">
        <label for="" class="form-label">센터 주소</label>
        <input type="text" class="form-control" :value="trainer.centerAddress" id="inputCenterName" readonly>
      </div>
      <div class="mb-3">
        <label for="" class="form-label">센터 정보</label>
        <input type="text" class="form-control" :value="trainer.centerInfo" id="inputCenterName" readonly>
      </div>
      <!-- 트레이너로 로그인했을 경우 아래 두가지만 수정가능하도록 수정예정     -->
      <div class="mb-3">
        <label for="" class="form-label">트레이너 정보</label>
        <input type="text" class="form-control" :value="trainer.trainerInfo" id="inputCenterName">
      </div>
      <div class="mb-3">
        <label for="" class="form-label">트레이너 상세 정보</label>
        <input type="text" class="form-control" :value="trainer.infoDetail" id="inputCenterName">
      </div>


    </div>
  </div>


</template>

<script setup>

import {onMounted, ref} from "vue";
import axios from "axios";

const trainer = ref({})

const props = defineProps({
  trainerId : {
    type: [Number, String],
    required:true
  },
});

onMounted(() => {
  axios.get(`/api/trainer/get/${props.trainerId}`, {

  })
      .then((response) => {
        console.log(response)
        trainer.value = response.data;
      })
      .catch((error) => {
        if(error.response) {
          alert(error.response.data.message)
        }
      })
})

</script>

<style scoped>

h1, div {
  color : white;
}

</style>
