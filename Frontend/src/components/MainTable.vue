<script setup>
import {onMounted, ref} from 'vue'
import {VueGoodTable} from 'vue-good-table-next'
import 'vue-good-table-next/dist/vue-good-table-next.css'

const columns = [
  {label: 'Фото', field: 'img', sortable: false},
  {label: 'Имя', field: 'name', sortable: true},
  {label: 'Возраст', field: 'age', sortable: true},
]

const rows = ref([])

onMounted(async () => {
  // временные данные, можно заменить fetch'ом
  rows.value = [
    {img: 'https://placekitten.com/60/60', name: 'Алиса', age: 23},
    {img: 'https://placekitten.com/61/61', name: 'Боб', age: 31},
    {img: 'https://placekitten.com/62/62', name: 'Чарли', age: 27},
  ]
})
</script>

<template>
  <div class="max-w-4xl mx-auto bg-white shadow rounded-2xl p-6">
    <h1 class="text-2xl font-bold mb-4 text-center">Пользователи</h1>

    <VueGoodTable
        :columns="columns"
        :rows="rows"
        style-class="vgt-table striped bordered"
    >
      <template #table-row="props">
        <span v-if="props.column.field === 'img'">
          <img
              :src="props.row.img"
              width="50"
              height="50"
              class="rounded-xl shadow-sm"
          />
        </span>
        <span v-else>
          {{ props.formattedRow[props.column.field] }}
        </span>
      </template>
    </VueGoodTable>
  </div>
</template>

<style scoped>
.vgt-table {
  border-radius: 12px;
  overflow: hidden;
}
</style>
