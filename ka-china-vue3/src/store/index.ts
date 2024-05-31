import { createPinia } from 'pinia';
import type { App } from 'vue';
import { createPersistedState } from 'pinia-plugin-persistedstate';

const store = createPinia();
store.use(
  createPersistedState({
    storage: sessionStorage,
  }),
);

export const setupStore = (app: App<Element>) => {
  app.use(store);
};

export { store };
