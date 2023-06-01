<template>
  <h1>검색창 만드는중</h1>
  <div class="search-container">
    <input v-model="searchCenter"/>
    <button @click="searchCenterBtn">센터 검색</button>
    
    <div ></div>
    
  </div>
</template>

<script setup>
import axios from "axios";
import { ref } from "vue";


    const searchCenter = ref('');

    const searchCenterBtn = async() => {
        const response = await axios.get(url+"center/search")
    }
</script>

<style scoped>
.search-container {
  margin-left: 8vh;
  margin-right: 8vh;
  width: auto;
  height: auto;
  min-height: 100vh;
}
</style>