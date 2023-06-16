<template>
  <div v-if="isLoading">
    <div class="loader">
      <div class="spinner-border text-light" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>
  </div>
  <div v-else>

    <hr>

    <div class="container-fluid">
      <table class="card" v-if="users.length > 0">
        <h1>고객리스트</h1>
        <thead class="card-header">
        <tr class="list-group-item d-flex justify-content-between">
          <th>#</th>
          <th>이름</th>
          <th>생년월일</th>
          <th>담당 트레이너</th>
          <th>이용 기간</th>
          <th>남은 PT 횟수</th>
        </tr>
        </thead>
        <tbody class="card-body contentAlign">
        <tr class="list-group-item d-flex justify-content-between" v-for="user in users" :key="user.id">
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
          <td>{{ user.startDate }} ~ {{ user.endDate }}</td>
          <td>{{ user.remainingPT }}</td>
        </tr>
        </tbody>
      </table>
    </div>
    <hr>

    <h1>트레이너</h1>
    <table class="table" v-if="trainers.length > 0">
      <thead>
      <tr>
        <th>#</th>
        <th>이름</th>
        <th>센터 이름</th>
        <th>센터 주소</th>
        <th>정보</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="trainer in trainers" :key="trainer.id">
        <td>{{ trainer.id }}</td>
        <td>
          <div v-if="authority === 2">
            <router-link :to="{ name: 'trainerCalendar', params: { trainerId: trainer.id } }">
              {{ trainer.name }}
            </router-link>
          </div>
          <div v-if="authority === 1">
            <router-link :to="{ name: 'trainerDetail', params: { trainerId: trainer.id } }">
              {{ trainer.name }}
            </router-link>
          </div>
          <div>
          </div>
        </td>
        <td>{{ trainer.centerName }}</td>
        <td>{{ trainer.centerAddress }}</td>
        <td>{{ trainer.trainerInfo }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import axios from 'axios'
import Cookies from 'vue-cookies'

const users = ref([])
const trainers = ref([])
const authority = ref('')
const isLoading = ref(true)

onMounted(async () => {
  setTimeout(() => {
    isLoading.value = false;
  }, 200);
  try {
    const token = Cookies.get('accessToken')

    const [authorityResponse, trainersResponse, usersResponse] = await Promise.all([
      axios.post('/api/user/getAuthority', {}, {
        headers: {
          Authorization: token
        }
      }),
      axios.get('/api/authTrainer', {
        headers: {
          Authorization: token
        }
      }),
      axios.get('/api/authUser', {
        headers: {
          Authorization: token
        }
      })
    ])

    authority.value = authorityResponse.data
    trainers.value = trainersResponse.data
    users.value = usersResponse.data
  } catch (error) {
    console.error(error)
    alert(error.response.data.message)
  }
})
</script>

<style scoped>
.table {
  color: white;
}

.loader {
  position: fixed;
  z-index: 999;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.card,
td {
  padding: 10px;
  text-align: left;
}


</style>