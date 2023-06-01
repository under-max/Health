<template>
  <div>
    <h1>일정 관리</h1>
    <div class="calendar" >
      <div class="header">
        <button @click="previousMonth">&lt;</button>
        <h2>{{ currentMonth }}</h2>
        <button @click="nextMonth">&gt;</button>
      </div>
      <table>
        <thead>
        <tr>
          <th v-for="day in daysOfWeek" :key="day">{{ day }}</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="week in calendar" :key="week">
          <td v-for="day in week" :key="day.date" :class="{ 'today': day.isToday, 'selected': isSelected(day) }">
            <div>{{ day.date }}</div>
            <div class="event" v-if="day.events.length >= 0 ">
              <div v-if="day.events.length > 0">
                <ul v-for="event in day.events" :key="event.date">이름 :{{ event.name }} 시간:{{event.time}} 남은 PT:{{event.pt}}</ul>
              </div>
            </div>
            <div class="event" v-if="day.date > 0">
              <!-- Button trigger modal -->
              <button @click="openModal(day)" type="button" class="btn btn-primary" data-bs-toggle="modal">
                일정 등록
              </button>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div>

    </div>


    <!-- 모달 창 -->
    <div v-if="showSchedule">
      <div class="modal-overlay" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h1 class="modal-title fs-5" id="exampleModalLabel">일정 등록</h1>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"
                      @click="closeModal"></button>
            </div>
            <div class="modal-body">
              <!-- 모달 내용 -->
              <h3>고객리스트</h3>
              <p>클릭된 날짜: {{ selectedDate }}</p>
              <!-- 여기에 일정 등록 폼 등을 추가할 수 있습니다. -->
              이름: <select v-model="userSelect">
              <option disabled selected>고객리스트</option>
              <option v-for="user in list" :value="user">{{ user.name }}</option>
            </select>
              <br>
              시간: <select v-model="timeSelect">
              <option disabled selected>시간</option>
              <option v-for="time in 24" :key="time">{{ time }}</option>

            </select>
              <p>남은 PT 횟수 : {{ userSelect.remainingPT }}</p>
            </div>
            <div class="modal-footer">
              <!-- 모달 닫기 버튼 -->
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" @click="closeModal">닫기</button>
              <button type="button" class="btn btn-primary" @click="saveModal">저장</button>
            </div>
          </div>
        </div>
      </div>
    </div>


    <div v-for="user in list">
      <p>{{ user.id }} {{ user.name }} {{ user.remainingPT }}</p>
    </div>
  </div>


</template>

<script setup>
import {ref, computed, onMounted, defineProps} from 'vue';
import axios from "axios";


const showSchedule = ref(false);
const userSelect = ref({
  name: '',
  remainingPT: ''
});
const timeSelect = ref("")
const selectedDate = ref(null);
const openModal = (day) => {
  selectedDate.value = new Date(year.value, month.value, day.date);
  showSchedule.value = true;
};
// const eventUser = userSelect.value.name;

const saveModal = () => {
  // 선택된 날짜에 일정 추가
  const eventDate = selectedDate.value;
  const event = {
    date: eventDate,
    name: userSelect.value.name,
    time: timeSelect.value,
    pt: userSelect.value.remainingPT
  };
  console.log(userSelect.value.name);
  console.log(timeSelect.value)
  console.log(userSelect.value.remainingPT)
  events.value.push(event);

  closeModal();
};

const closeModal = () => {
  showSchedule.value = false;
}


const props = defineProps({
  trainerId: {
    type: [Number, String],
    required: true
  },
});

// 해당 트레이너의 고객리스트 저장
const list = ref({
  id: '',
  name: '',
  remainingPT: '',
});



onMounted(() => {
  console.log()
  axios.get(`/api/responsibleUserList/${props.trainerId}`, {})
      .then((response) => {
        list.value = response.data;
        // console.log('---------------------')
        // console.log(list.value[0].authority)
        // if(list.value[0].authority == 1){
        //   changeAuth()
        // }
      })
      .catch((error) => {
        if (error.response) {
          alert(error.response.data.message);
        }
      })
})

// 달력 데이터 초기화
const currentDate = new Date();
const year = ref(currentDate.getFullYear());
const month = ref(currentDate.getMonth());
const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];

// 일정 데이터 초기화
const events = ref([

]);

const realEvents = ref({})

// 현재 월 계산
const currentMonth = computed(() => {
  const date = new Date(year.value, month.value);
  return `${date.getFullYear()}년 ${date.getMonth() + 1}월`;
});

// 달력 생성
const calendar = computed(() => {
  const firstDayOfMonth = new Date(year.value, month.value, 1).getDay();
  const daysInMonth = new Date(year.value, month.value + 1, 0).getDate();

  const days = [];
  let day = 1;

  for (let i = 0; i < 6; i++) {
    const week = [];

    for (let j = 0; j < 7; j++) {
      if (i === 0 && j < firstDayOfMonth) {
        week.push({date: '', isToday: false, events: []});
      } else if (day <= daysInMonth) {
        const currentDate = new Date(year.value, month.value, day);
        const isToday = currentDate.toDateString() === new Date().toDateString();
        const eventsOnDay = events.value.filter(event => event.date.toDateString() === currentDate.toDateString());

        week.push({date: day, isToday, events: eventsOnDay});
        day++;
      } else {
        week.push({date: '', isToday: false, events: []});
      }
    }

    days.push(week);
    if (day > daysInMonth) break;
  }

  return days;
});

// 이전 월로 이동
const previousMonth = () => {
  if (month.value === 0) {
    year.value--;
    month.value = 11;
  } else {
    month.value--;
  }
};

// 다음 월로 이동
const nextMonth = () => {
  if (month.value === 11) {
    year.value++;
    month.value = 0;
  } else {
    month.value++;
  }
};

// 일정 추가
const newEvent = ref('');
const addEvent = day => {
  if (newEvent.value.trim() !== '') {
    const eventDate = new Date(year.value, month.value, day.date);
    events.value.push({date: eventDate, event: newEvent.value});
    newEvent.value = '';
  }
};

// 일정 선택 여부 확인
const isSelected = day => {
  const selectedDate = new Date(year.value, month.value, day.date);
  return events.value.some(event => event.date.toDateString() === selectedDate.toDateString());
};
</script>

<style scoped>
.calendar {
  width: auto;
  margin: 0 auto;
}

.header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
}

button {
  margin: 0 5px;
  cursor: pointer;
}

.button-group {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 15px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  text-align: center;
  padding: 10px;
  border: 1px solid #ccc;
}

th {
  font-weight: bold;
}

.today {
  background-color: #e8f0fe;
  font-weight: bold;
}

.selected {
  background-color: #d9e6fc;
}

.event {
  margin-top: 5px;
}

input[type='text'] {
  width: 100%;
  padding: 5px;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.modal-content {
  width: 600px;
  max-height: 400px;
  background-color: #fff;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.modal-title {
  font-size: 20px;
  font-weight: bold;
}

.modal-body {
  flex-grow: 1;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  gap: 10px;
}

.modal-footer button {
  padding: 8px 16px;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
}

.modal-footer button.btn-secondary {
  background-color: #ccc;
  color: #fff;
  border: none;
}

.modal-footer button.btn-primary {
  background-color: #007bff;
  color: #fff;
  border: none;
}

a {
  color: #007bff;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}
</style>


