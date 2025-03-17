use std::borrow::Borrow;

/// A munger which XORs a key with some data
#[derive(Clone)]
pub struct Xorcism<'a> {
    key: &'a [u8],
    position: usize,
}

impl<'a> Xorcism<'a> {
    /// Create a new Xorcism munger from a key
    ///
    /// Should accept anything which has a cheap conversion to a byte slice.
    pub fn new<Key>(key: &'a Key) -> Xorcism<'a>
    where
        Key: AsRef<[u8]> + ?Sized,
    {
        Xorcism {
            key: key.as_ref(),
            position: 0,
        }
    }

    /// XOR each byte of the input buffer with a byte from the key.
    ///
    /// Note that this is stateful: repeated calls are likely to produce different results,
    /// even with identical inputs.
    pub fn munge_in_place(&mut self, data: &mut [u8]) {
        for byte in data.iter_mut() {
            *byte ^= self.key[self.position];
            self.position = (self.position + 1) % self.key.len();
        }
    }

    /// XOR each byte of the data with a byte from the key.
    ///
    /// Note that this is stateful: repeated calls are likely to produce different results,
    /// even with identical inputs.
    ///
    /// Should accept anything which has a cheap conversion to a byte iterator.
    /// Shouldn't matter whether the byte iterator's values are owned or borrowed.
    pub fn munge<'b, Data>(&'b mut self, data: Data) -> impl Iterator<Item = u8> + 'b
    where
        Data: IntoIterator,
        Data::Item: Borrow<u8>,
        <Data as IntoIterator>::IntoIter: 'b,
    {
        data.into_iter().map(|byte| {
            let result = *byte.borrow() ^ self.key[self.position];
            self.position = (self.position + 1) % self.key.len();
            result
        })
    }

    #[cfg(feature = "io")]
    /// Create a reader that will XOR the bytes from the underlying reader with the key
    pub fn reader<R: std::io::Read + 'a>(self, reader: R) -> impl std::io::Read + 'a {
        XorcismReader { xorcism: self, reader }
    }

    #[cfg(feature = "io")]
    /// Create a writer that will XOR the bytes written to it with the key and then write them to the underlying writer
    pub fn writer<W: std::io::Write + 'a>(self, writer: W) -> impl std::io::Write + 'a {
        XorcismWriter { xorcism: self, writer }
    }
}

#[cfg(feature = "io")]
struct XorcismReader<'a, R> {
    xorcism: Xorcism<'a>,
    reader: R,
}

#[cfg(feature = "io")]
impl<'a, R: std::io::Read> std::io::Read for XorcismReader<'a, R> {
    fn read(&mut self, buf: &mut [u8]) -> std::io::Result<usize> {
        let bytes_read = self.reader.read(buf)?;
        if bytes_read > 0 {
            self.xorcism.munge_in_place(&mut buf[..bytes_read]);
        }
        Ok(bytes_read)
    }
}

#[cfg(feature = "io")]
struct XorcismWriter<'a, W> {
    xorcism: Xorcism<'a>,
    writer: W,
}

#[cfg(feature = "io")]
impl<'a, W: std::io::Write> std::io::Write for XorcismWriter<'a, W> {
    fn write(&mut self, buf: &[u8]) -> std::io::Result<usize> {
        let mut munged = Vec::with_capacity(buf.len());
        munged.extend(self.xorcism.munge(buf));
        self.writer.write(&munged)
    }

    fn flush(&mut self) -> std::io::Result<()> {
        self.writer.flush()
    }
}
