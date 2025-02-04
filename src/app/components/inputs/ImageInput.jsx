import { Button, Image, Input } from '@nextui-org/react';
import { MdKeyboardArrowLeft, MdKeyboardArrowRight } from 'react-icons/md';

export default function ImageInput({ customImage, setCustomImage, imageKey, setImageKey, grids, viewOnly }) {
  const passImage = (increment) => {
    setImageKey((prevKey) => (prevKey + increment + grids.length) % grids.length);
  };

  return (
    <>
      <Image className="aspect-[6/9]" style={{ height: 'auto' }} width={600} height={900} alt="Hero Image" src={customImage || grids[imageKey] || 'https://i.imgur.com/wCx4mHs.png'} />
      {!viewOnly && (
        <>
          <Input
            label="Imagen personalizada"
            placeholder="Introduzca una URL"
            type="text"
            variant="bordered"
            onChange={(e) => setCustomImage(e.target.value)}
            value={customImage}
            required
            disabled={viewOnly}
          />
          {customImage ? (
            <Button onClick={() => setCustomImage('')} className="h-10 w-full text-xl block">
              Borrar imagen personalizada
            </Button>
          ) : (
            <div className="flex justify-between items-center gap-2">
              <Button onClick={() => passImage(-1)} className="flex items-center justify-center text-4xl" isIconOnly>
                <MdKeyboardArrowLeft />
              </Button>
              <div className="w-1/4 flex flex-grow sm:flex-grow-0">
                <Input
                  value={imageKey + 1}
                  onChange={(e) => {
                    setImageKey(e.target.value - 1);
                  }}
                  endContent={
                    <div className={'flex-row flex gap-2'}>
                      <p>{' / '}</p>
                      <p>{grids.length}</p>
                    </div>
                  }
                  classNames={{
                    inputWrapper: 'h-unit-10',
                  }}
                />
              </div>
              <Button onClick={() => passImage(+1)} className="flex items-center justify-center text-4xl" isIconOnly>
                <MdKeyboardArrowRight />
              </Button>
            </div>
          )}
        </>
      )}
    </>
  );
}
