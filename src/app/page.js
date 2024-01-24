import Image from 'next/image';

async function getData() {
  const res = await fetch('http://localhost:8080/api/games', { cache: 'no-store' });
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
    <main className="bg-gray-900 h-full">
      <div className="p-10 grid grid-cols-6 gap-3">
        {data.games.map((game) => (
          <div key={game.id} className="aspect-[3/5] bg-gray-600 rounded-md relative overflow-hidden group ">
            <div className="z-10 w-full h-full relative">
              <Image src={game.image} alt={game.image} layout="fill"></Image>
            </div>
            <div className="z-20 absolute bottom-0 w-full text-center font-bold uppercase bg-gray-900/40 h-[1/10] opacity-0 group-hover:opacity-100">{game.name}</div>
          </div>
        ))}
      </div>
    </main>
  );
}
