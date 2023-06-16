<script setup lang="js">
import { RouterLink, RouterView } from 'vue-router'
import {ref, watchEffect} from 'vue';
import logo from '@/assets/Logo.png';
import Header from './components/Header.vue';
const Login = ref(false);
</script>

<template>
  <Header></Header>
  <RouterView />
</template>

<style>
body{
  margin: 0px;
  background-color: #112470;
  background-image: linear-gradient(176deg, #112470 35%, #fff);
  background-repeat: no-repeat;
  min-height: 100vh;
  height: auto;
}
.custom-alert {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    padding: 20px;
    background-color: #3C689F;
    color: #fff;
    text-align: center;
    font-size: 18px;
    z-index: 9999;
}
</style>
