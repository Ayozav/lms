//базовый клиент + учет портов + аутенфикации

const API_BASE = {
  main: 'http://127.0.0.1:4040/v1',
  gradeUser: 'http://127.0.0.1:4050/v1',  // для user и grade
};

async function request<T>(
  url: string,
  options: RequestInit = {}
): Promise<T> {
  const response = await fetch(url, {
    headers: {
      'Content-Type': 'application/json',
      // Если нужен токен из Logto:
      // 'Authorization': `Bearer ${getAccessToken()}`,
      ...options.headers,
    },
    ...options,
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`HTTP ${response.status}: ${errorText}`);
  }

  // Если ответ пустой (204 No Content) – вернём null
  if (response.status === 204) return null as T;
  return response.json();
}

export default API_BASE