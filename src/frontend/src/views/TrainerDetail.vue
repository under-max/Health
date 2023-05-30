<template>
  <div>
    <h1>일정 관리</h1>
    <div class="calendar">
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
<!--            <div class="event" v-if="day.events.length > 0">-->
<!--              <ul>-->
<!--                <li v-for="event in day.events" :key="event">{{ event }}</li>-->
<!--              </ul>-->
<!--            </div>-->
            <div class="event" v-if="day.date > 0">

            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

        <div v-for="user in list">
          <p>{{user.id}} {{user.name}} {{user.remainingPT}}</p>
        </div>


    <select v-model="userSelect">
      <option value="" disabled selected>고객리스트</option>
      <option v-for="user in list" :value="user.value">{{user.name}}</option>
    </select>

    {{list.remainingPT}}
  </div>
</template>

<script setup>
import {ref, computed, onMounted, defineProps} from 'vue';
import axios from "axios";

const userSelect = ref("");
const props = defineProps({
  trainerId : {
    type : [Number, String],
    required : true
  },
});

// 해당 트레이너의 고객리스트 저장
const list = ref({
  id : '',
  name : '',
  remainingPT : ''
});


onMounted(() => {
  console.log()
  axios.get(`/api/test/trainerDetail/${props.trainerId}`, {})
    .then((response) => {
      list.value = response.data;
      console.log(response.data);
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
  // {
  //   date: new Date(year.value, month.value, 5),
  //   event: '회의',
  // },
  // {
  //   date: new Date(year.value, month.value, 15),
  //   event: '생일',
  // },
]);

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
        week.push({ date: '', isToday: false, events: [] });
      } else if (day <= daysInMonth) {
        const currentDate = new Date(year.value, month.value, day);
        const isToday = currentDate.toDateString() === new Date().toDateString();
        const eventsOnDay = events.value.filter(event => event.date.toDateString() === currentDate.toDateString());

        week.push({ date: day, isToday, events: eventsOnDay });
        day++;
      } else {
        week.push({ date: '', isToday: false, events: [] });
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
    events.value.push({ date: eventDate, event: newEvent.value });
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
  width: 400px;
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
  color : red;
}

.event {
  margin-top: 5px;
}

input[type='text'] {
  width: 100%;
  padding: 5px;
}


</style>