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
          <h3 class="trainerInfo">정보: {{ trainerDetail.info }}</h3>
          <h3 class="trainerInfoDetail">상세정보: {{trainerDetail.infoDetail}}</h3>
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

.trainerDetailContainer {
    display: flex;
    justify-content: center;
    align-items: center;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.7);
    z-index: 9999;
  }

  .trainerDetailDiv {
    background-color: #fff;
    min-width: 40vw;
    min-height: 30vh;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  }

  .x-mark-div {
    display: flex;
    justify-content: flex-end;
  }

  .x-mark {
    cursor: pointer;
    color: #888;
  }

  .trainerDetailContentDiv {
    display: flex;
    align-items: center;
    margin-top: 20px;
    max-width: 50vw;
  }

  .trainerDetailImageBox {
    width: 40%;
    max-width: 300px;
    height: auto;
    margin-right: 20px;
  }

  .trainerDetailImage {
    width: 100%;
    height: 100%;
    object-fit: contain;
    border-radius: 8px;
  }

  .trainerDetailTxt {
    flex: 1;
  }

  .trainerName {
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 10px;
  }

  .trainerInfo,
  .trainerInfoDetail {
    font-size: 18px;
    margin-bottom: 10px;
  }
</style>