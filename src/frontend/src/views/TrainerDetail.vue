<template>
  <div v-if="isLoading">
    <div class="loader">
      <div class="spinner-border text-light" role="status">
        <span class="visually-hidden">Loading..</span>
      </div>
    </div>
  </div>
  <div v-else>
    <div class="container">
      <div class="card" >

        <h1>트레이너 상세 페이지</h1>
        <div class="mb-3 justify-content-center trainerImg">
          <label for="" class="form-label"></label>
          <img
              :src="`https://bucket0503-1000011011001.s3.ap-northeast-2.amazonaws.com/health/${trainer.centerId}/${trainer.trainerId}/${trainer.fileName}`"
              alt=""
          />
        </div>
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
        <div class="mb-3">
          <label for="" class="form-label">트레이너 정보</label>
          <input type="text" class="form-control" :value="trainer.trainerInfo" id="inputCenterName" readonly>
        </div>
        <div class="mb-3">
          <label for="" class="form-label">트레이너 상세 정보</label>
          <input type="text" class="form-control" :value="trainer.infoDetail" id="inputCenterName" readonly>
        </div>
      </div>
    </div>
  </div>

</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
const trainer = ref({})
const isLoading = ref(true)
const props = defineProps({
  trainerId: {
    type: [Number, String],
    required: true
  },
});

onMounted(() => {
  setTimeout(() => {
    isLoading.value = false;
  }, 600)
  axios.get(`/api/trainer/get/${props.trainerId}`, {})
      .then((response) => {
        trainer.value = response.data;
      })
      .catch((error) => {
        if (error.response) {
          alert(error.response.data.message)
        }
      })
})

</script>

<style scoped>
.trainerImg {
  margin-left: auto;
  margin-right: auto;
  overflow: hidden;
}

.trainerImg img {
  max-height: 40vw;
}

.loader {
  position: fixed;
  z-index: 999;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.form-control {
  width: 700px;
}

.form-control input:focus{
  outline: none;
}

.container {
  max-width: 800px;
}

.card {
  margin : 3px;
  padding: 8px;
}
</style>
