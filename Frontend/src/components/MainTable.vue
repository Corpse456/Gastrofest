<script setup>
import {onMounted, ref} from 'vue'
import {VueGoodTable} from 'vue-good-table-next'
import 'vue-good-table-next/dist/vue-good-table-next.css'

const columns = [
  {label: 'Фото', field: 'imageLink', sortable: false},
  {label: 'Вес', field: 'weight', sortable: true, type: 'number'},
  {label: 'На вынос', field: 'eatOutside', sortable: false},
  {label: 'Бронь', field: 'bookingPossibility', sortable: false},
  {label: 'Гастрофест', field: 'gastrofest', sortable: true},
  {label: 'Заведение', field: 'participant', sortable: true},
  {label: 'Ресторан', field: 'restaurant', sortable: false},
]

const rows = ref([])

const API_URL = '/api/gastroset';

onMounted(async () => {
  try {
    const response = await fetch(API_URL)
    if (!response.ok) throw new Error('Ошибка загрузки данных')
    rows.value = await response.json()
  } catch (err) {
    console.error('Ошибка при получении данных:', err)
  }
})
</script>

<template>
  <div class="max-w-6xl mx-auto bg-white shadow rounded-2xl p-6">
    <h1 class="text-2xl font-bold mb-4 text-center">Меню</h1>

    <VueGoodTable
        :columns="columns"
        :rows="rows"
        style-class="vgt-table striped bordered"
    >
      <template #table-row="props">
        <!-- 🖼 Фото -->
        <span v-if="props.column.field === 'imageLink'">
          <a
              :href="props.row.url"
              target="_blank"
              rel="noopener noreferrer"
              title="Открыть страницу"
          >
            <img :src="props.row.imageLink" class="thumb"/>
          </a>
        </span>

        <!-- ✅❌ Булевые поля -->
        <span v-else-if="['eatOutside', 'bookingPossibility', 'restaurant'].includes(props.column.field)">
          <span
              v-if="props.row[props.column.field]"
              class="text-green-600 text-lg"
              title="Да"
          >✅</span>
          <span
              v-else
              class="text-red-500 text-lg"
              title="Нет"
          >❌</span>
        </span>

        <!-- 📄 Остальные поля -->
        <span v-else>
          {{ props.formattedRow[props.column.field] }}
        </span>
      </template>
    </VueGoodTable>
  </div>
</template>

<style scoped>
:deep(.vgt-table th),
:deep(.vgt-table td) {
  text-align: center;
  vertical-align: middle;
}
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
