<script setup>
import {onMounted, ref} from 'vue'
import {VueGoodTable} from 'vue-good-table-next'
import 'vue-good-table-next/dist/vue-good-table-next.css'

const columns = [
  {label: 'Фото', field: 'img', sortable: false},
  {label: 'Вес', field: 'weight', sortable: true},
  {label: 'На вынос', field: 'takeaway', sortable: false},
  {label: 'Бронь', field: 'booking', sortable: false},
  {label: 'Гастрофест', field: 'festival', sortable: true},
  {label: 'Заведение', field: 'place', sortable: true},
  {label: 'Ресторан', field: 'restaurant', sortable: true},
]

const rows = ref([])

onMounted(() => {
  rows.value = [
    {
      img: 'https://gastrofest.by/sites/default/files/0015_51.jpg',
      weight: '250 г',
      takeaway: 'Да',
      booking: 'Нет',
      festival: 'Burger Fest',
      place: 'Бар «12»',
      restaurant: 'Meat House',
      url: 'https://gastrofest.by/node/14936',
    },
    {
      img: 'https://gastrofest.by/sites/default/files/0001_123.jpg',
      weight: '300 г',
      takeaway: 'Нет',
      booking: 'Да',
      festival: 'Pizza Fest',
      place: 'Pizzeria Uno',
      restaurant: 'Cucina Italiana',
      url: 'https://gastrofest.by/node/14938',
    },
  ]
})
</script>

<template>
  <div class="max-w-6xl mx-auto bg-white shadow rounded-2xl p-6">
    <h1 class="text-2xl font-bold mb-4 text-center">Блюда</h1>

    <VueGoodTable
        :columns="columns"
        :rows="rows"
        style-class="vgt-table striped bordered"
    >
      <template #table-row="props">
        <!-- 👇 если колонка "Фото", делаем кликабельную ссылку -->
        <span v-if="props.column.field === 'img'">
          <a
              :href="props.row.url"
              target="_blank"
              rel="noopener noreferrer"
              title="Открыть страницу"
          >
            <img
                :src="props.row.img"
                class="thumb"
            />
          </a>
        </span>
        <span v-else>
          {{ props.formattedRow[props.column.field] }}
        </span>
      </template>
    </VueGoodTable>
  </div>
</template>

<style scoped>
.thumb {
  width: 300px;
  border-radius: 8px;
  object-fit: cover;
  cursor: pointer;
  transition: transform 0.15s ease;
}

.thumb:hover {
  transform: scale(1.05);
}
</style>
