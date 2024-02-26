import {Button, Image, Input} from "@nextui-org/react";

export default function ImageInput({ customImage, setCustomImage, imageKey, setImageKey, grids, passImage }) {
    return (
        <>
            <Image
              width={600}
              height={900}
              alt="Hero Image"
              src={customImage || grids[imageKey] }
            />
            {/* IMAGE INPUT */}
            <Input
              label="Custom Image"
              placeholder="Enter a URL"
              type="text"
              variant="bordered"
              onChange={(e) => setCustomImage(e.target.value)}
              value={customImage}
              required
            />
            {customImage ?
              <Button onClick={() => setCustomImage("")} className="h-10 w-full text-xl block z-40">
                  Erase Custom Image
              </Button>
              :
              <div className="flex justify-between items-center">
                  <Button onClick={() => passImage(-1)} className="h-full w-1/4 text-4xl block z-40">
                      {"<"}
                  </Button>
                  <div className={"w-1/4"}>
                      <Input
                        value={imageKey + 1}
                        onChange={(e) => {
                            setImageKey(e.target.value - 1);
                        }}
                        endContent={
                            <div className={"flex-row flex gap-2"}>
                                <p>{" / "}</p>
                                <p>{grids.length}</p>
                            </div>}
                      >
                      </Input>
                  </div>
                  <Button onClick={() => passImage(+1)} className="h-full w-1/4 text-4xl block z-40">
                      {">"}
                  </Button>
              </div>
            }
        </>)
}