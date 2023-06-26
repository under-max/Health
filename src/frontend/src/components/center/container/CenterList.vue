<template>
    <div class="centerContainer">
      <div class="centerListImgDiv">        
          <img
              :src="`https://bucket0503-1000011011001.s3.ap-northeast-2.amazonaws.com/health/${centerList.id}/${centerList.fileName[0]}`"
              class="centerListImage"
              alt="Center Image"
          />
      </div>
      <div class="centerListText">
          <h2>상호: {{ centerList.name }}</h2>
          <hr>
          <h2>주소: {{ centerList.address }}</h2>
          <h2>정보: {{ centerList.info }}</h2>
          <i class="fas fa-star fa-2x star">{{centerList.star.toFixed(2)}}</i>        
      </div>
    </div>


</template>

<script setup>
import { defineProps} from 'vue';


const props = defineProps({
    centerList: Object,
    user: Object,
})

</script>

<style scoped>
.centerContainer {
  background-color: white;
  width: 100%;
  max-width: 26%;
  min-width: 380px;

  margin-bottom: 5%;
  text-align: center;
  border: 1px solid gray;
  border-radius: 30px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.centerListImgDiv {
  height: 300px;
  overflow: hidden;
}

.centerListImage {
  height: 100%;
  width: 100%;
  object-fit: cover;
  object-position: center;
}

.centerListText {
  padding: 20px;
}

.centerListText h2 {
  margin: 0 0 10px;
  font-size: 1.4rem;
}

.centerListText hr {
  margin: 10px 0;
  border: none;
  border-top: 1px solid #ccc;
}

.star {
  color: rgb(245, 245, 0);
}
</style>