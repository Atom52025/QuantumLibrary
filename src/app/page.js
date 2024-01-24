import Link from 'next/link';

async function getData() {
  const res = await fetch('http://localhost:8080/api/games');
  // The return value is *not* serialized
  // You can return Date, Map, Set, etc.

  if (!res.ok) {
    // This will activate the closest `error.js` Error Boundary
    throw new Error('Failed to fetch data');
  }

  return res.json();
}

export default async function Page() {
  const data = await getData();
  return (
    <main>
      <div>
        {data.games.map((game) => (
          <li key={game.id}>
            <div>{game.name}</div>
          </li>
        ))}
      </div>
    </main>
  );
}
