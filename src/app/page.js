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
  const gridClass = 'aspect-[6/9] bg-gray-600 rounded-md relative overflow-hidden group';
  return (
    <main className="bg-gray-900 min-h-full">
      <div className="p-10 grid grid-cols-6 gap-3">
        {data.games.map((game) => (
          <div key={game.id} className={gridClass}>
            <div className="z-10 w-full h-full relative">
              <Image src={game.image} alt={game.image} fill priority={true}></Image>
            </div>
            <div className="z-20 absolute bottom-0 w-full text-center font-bold uppercase bg-gray-900/40 p-1 opacity-0 group-hover:opacity-100">{game.name}</div>
          </div>
        ))}
        <div className={gridClass + ' border border-dashed border-gray-400 flex justify-center items-center'}>
          <div className="text-center">
            <div className="text-4xl font-bold">+</div>
            <div className="text-xl">Add Game</div>
          </div>
        </div>
      </div>
    </main>
  );
}
