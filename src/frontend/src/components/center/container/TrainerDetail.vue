<template>
    <div class="trainerDetailContainer" v-if="trainerDetail.length !== 0">
    <div class="trainerDetailDiv">
      <div class="x-mark-div">
        <i class="fa-regular fa-circle-xmark fa-2x x-mark" @click="closeModal"></i>
      </div>

      <div class="trainerDetailContentDiv">
        <div class="trainerDetailImageBox">
          <img
            :src="`https://bucket0503-1000011011001.s3.ap-northeast-2.amazonaws.com/health/${trainerDetail.centerId}/${trainerDetail.id}/${trainerDetail.fileName}`"
            alt=""
            class="trainerDetailImage"
          />
        </div>
        <div class="trainerDetailTxt">
          <h3 class="trainerName">이름: {{ trainerDetail.name }}</h3>
          <h3 class="trainerInfo">트레이너 정보: {{ trainerDetail.info }}</h3>
        </div>
      </div>
      
    </div>

  </div>
</template>

<script setup>
import { defineProps, defineEmits} from "vue";

const props = defineProps({
  trainerDetail: Object,
  trainerClose: Boolean,
});

const emit = defineEmits(["closeTrainerDetailModal"]);

const closeModal = () => {
  emit("closeTrainerDetailModal");
};

</script>

<style scoped>
h1 {
  color: black;
}

.trainerDetailDiv {
  margin: 3vh;
  min-width: 680px;
  background: white;
  border-radius: 20px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.trainerDetailContentDiv {
  flex: 1;
  display: flex;
  text-align: center;
}

.trainerDetailTxt {
  flex: 1;
  padding: 2vh;
}

.trainerDetailImageBox {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.trainerDetailImageBox img {  
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.x-mark {
  color: red;
  cursor: pointer;
}

.x-mark-div {
  align-self: flex-end;
  margin-right: 2vh;
  padding-top: 2vh;
}

.trainerDetailContainer{
  margin: 0 3vh;
  border-radius: 15px;
}

.trainerName {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.trainerInfo {
  font-size: 18px;
  color: #333;
}
</style>