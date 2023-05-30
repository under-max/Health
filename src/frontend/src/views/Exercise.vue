<template>
  <h1>운동관리!</h1>
  <hr>
  <h1>고객리스트</h1>
  <table class="table">
    <thead>
    <tr>
      <th>#</th>
      <th>이름</th>
      <th>생년월일</th>
      <th>담당 트레이너</th>
      <th>이용 기간</th>
      <th>이용 기간</th>
      <th>남은 PT 횟수</th>
      <th>현재 상태</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="user in users" :key="user.id">
      <td>{{ user.id }}</td>
      <td>
        <div>
          <router-link :to="{ name: 'userDetail', params: { userId: user.id } }">
            {{ user.name }}
          </router-link>
        </div>
      </td>
      <td>{{ user.birthDate }}</td>
      <td>{{ user.trainerName }}</td>
      <td>{{ user.startDate }}</td>
      <td>{{ user.endDate }}</td>
      <td>{{ user.remainingPT }}</td>
      <td>{{ user.isInCenter }}</td>
    </tr>
    </tbody>
  </table>
  <hr>

  <h1>트레이너</h1>
  <table class="table">
    <thead>
    <tr>
      <th>#</th>
      <th>이름</th>
      <th>주소</th>
      <th>이메일</th>
      <th>핸드폰</th>
      <th>정보</th>
      <th>출근 상태</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="trainer in trainers" :key="trainer.id">
      <td>{{trainer.id}}</td>
      <td>
        <div>
          <router-link :to="{ name: 'trainerDetail', params: { trainerId: trainer.id } }">
          {{trainer.name}}
          </router-link>
        </div>
      </td>
      <td>{{trainer.address}}</td>
      <td>{{trainer.email}}</td>
      <td>{{trainer.phoneNumber}}</td>
      <td>{{trainer.info}}</td>
      <td>{{trainer.isInCenter}}</td>
    </tr>

    </tbody>

  </table>

</template>

<script setup>
import {onMounted, ref} from 'vue'
import axios from "axios";

const id = ref();
const name = ref();
const users = ref([])
const trainers = ref([]);

onMounted(() => {
  axios.get("/api/test/trainer/list", {})
      .then((response) => {
        console.log(response.data);
        trainers.value = response.data
      }).catch((error) => {
        console.log(error);
  });
});

onMounted(() => {
  axios.get("/api/test/user/lists", {})
      .then((response) => {
        console.log(response.data);
        users.value = response.data
      }).catch((error) => {
        console.log(error)
  });
});


</script>

<style>
.table {
  color: white;
}

h1 {
  color: white;
}
</style>