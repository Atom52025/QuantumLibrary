export const dynamic = 'force-dynamic'; // defaults to auto

const EXTERNAL_API_URL = process.env.NEXT_PUBLIC_API_URL;
export async function GETSIGNAL(url, token, signal) {
  // Create Query String
  const completeUrl = `${EXTERNAL_API_URL}/${url}`;

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
