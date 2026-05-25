// src/api/api.ts (или apiClient.ts)
const PORTS = {
  main: 4040,
  userGrade: 4050
};

export const BASE_URLS = {
  main: `http://127.0.0.1:${PORTS.main}/v1`,
  userGrade: `http://127.0.0.1:${PORTS.userGrade}/v1`
};

interface RequestOptions extends RequestInit {
  params?: Record<string, any>;
}

export async function request<T>(
  url: string,
  options: RequestOptions = {}
): Promise<T> {
  let fullUrl = url;
  if (options.params) {
    const searchParams = new URLSearchParams();
    Object.entries(options.params).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        searchParams.append(key, String(value));
      }
    });
    const queryString = searchParams.toString();
    if (queryString) {
      fullUrl += (fullUrl.includes('?') ? '&' : '?') + queryString;
    }
    delete options.params;
  }

  const response = await fetch(fullUrl, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`HTTP ${response.status}: ${errorText}`);
  }

  if (response.status === 204) return null as T;
  return response.json();
}

// Нет export default – только именованные экспорты