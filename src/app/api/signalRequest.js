export const dynamic = 'force-dynamic'; // defaults to auto

const INTERNAL_API_URL = process.env.NEXT_PUBLIC_INTERNAL_API_URL;
const EXTERNAL_API_URL = process.env.NEXT_PUBLIC_EXTERNAL_API_URL;

// Detects if its executing on client or in server
const API_URL = typeof window === 'undefined' ? INTERNAL_API_URL : EXTERNAL_API_URL;

export async function GETSIGNAL(url, token, signal) {
  // Create Query String
  const completeUrl = `${API_URL}/${url}`;

  // Fetch Data
  const res = await fetch(completeUrl, {
    signal,
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  if (!res.ok) {
    console.log('Failed to fetch data');
  }
  return res.json();
}
